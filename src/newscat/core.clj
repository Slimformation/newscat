(ns newscat.core
  (:require [clojure.string :as str]
            [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s])
  (:use [boilerpipe-clj.core]))

(defn get-article-text
  "Given a URL, extracts the article text and returns it as a string"
  [url]
  (get-text (slurp url)))

(def query-template
  "http://www.reddit.com/r/subreddit/new.json?sort=new&limit=100")

(def categories
  ["politics" "business" "technology"
   "sports" "science" "entertainment"])

(def db
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "database.db"
   })

(defn create-cat-table [category]
  (try (j/with-connection db
         (j/create-table category
                         [:url :text]
                         [:content :text]))
       (catch Exception e (println e))))

(defn make-tables
  "creates all the category tables in the database defined as 'db'"
  []
  (map #(create-cat-table %) categories))


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
