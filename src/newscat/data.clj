(ns newscat.data
  (:require [clojure.string :as string]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]
            [clj-http.client :as client]
            [clojure.data.json :as json])
  (:use [boilerpipe-clj.core]
        [newscat.db]))

(defn get-article-text
  "Given a URL, extracts the article text and returns it as a string"
  [url]
  (try (get-text (slurp url))
       (catch Exception e "")))

(def query-template
  "http://www.reddit.com/r/subreddit/new.json?sort=top&limit=50")


(defn run-query
  "Given a category, run a query and return results"
  [category query-template]
  (:body
   (try 
     (client/get
      (string/replace query-template
                      #"subreddit"
                      category))
     (catch Exception e (do (println e)
                            {:body {:error e}})))))


(defn extract-text
  "given the result of a query, extract the article text and return it
   as a list of maps of the form {:url url, :content article-text}"
  [query-result]
  (map #(hash-map :url (->> % :data :url)
                  :content (get-article-text (->> % :data :url)))
       (->> (json/read-str query-result :key-fn keyword)
            :data
            :children)))


(defn store-query
  "given a category and a query template, stores result of query in table"
  [category query-template]
  (let [res (extract-text (run-query category query-template))]
    (map #(j/insert! db category %)
         res)))

(defn query-and-store-all
  "runs queries and stores results for all categories"
  [categories query-template]
  (map #(store-query % query-template)
       categories))
