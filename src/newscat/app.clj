(ns newscat.app
  (:require [clojure.data.json :as json]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [noir.util.middleware :as nm]
            [newscat.train :as train]
            [clj-http.client :as client])
  (:use [compojure.core]
        [environ.core])
  (:import [org.jsoup Jsoup]))

(defn wrap-response
  "Helper for wrapping responses for Ring"
  [content status] {:status status
                    :headers {"Content-Type" "application/json"}
                    :body content})

(defn content-extract-sub
  "Sub function of newscat.app/content-extract"
  [url]
  (let [endpoint "http://www.readability.com/api/content/v1/parser?"
        token    (env :readability-parser-api-token)
        query    (str endpoint "url=" url "&token=" token)
        res      (client/get query)]
    (try (json/read-str (->> res :body)
                        :key-fn keyword)
         (catch Exception e {:error e}))))

(defn content-extract
  "Uses Readability Parser API to extract information about url."
  [url]
  (let [res (content-extract-sub url)]
    (if (contains? res :error)
      ""
      (->> res :content Jsoup/parse .text))))


(def categorizer
  (train/newscat))

(defn categorize
  "Request handler"
  [params]
  (try
    (wrap-response (json/write-str  
                    (categorizer (->> params
                                      vec
                                      flatten
                                      second
                                      content-extract)))
                   202)
    (catch Exception e
      (wrap-response e
                     409))))


(def app-routes
  "Vector of forms that define how routes in the app behave"
  [(GET "/categorize.json" {params :params} (categorize params))
   (route/not-found "Categorizer")])

(def app
  "Handler for the application, for Ring."
  (nm/app-handler app-routes))

