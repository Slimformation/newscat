(ns newscat.db
  (:require [clojure.java.jdbc :as j]
            [clojure.java.jdbc.sql :as s]))

(def categories
  "seq of categories about which train the categorizer"
  ["politics" "business" "technology"
   "sports" "science" "entertainment"])

(def db
  "definition of the dataset collection database"
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "resources/database.db"
   })

(defn create-cat-table
  "For given db and category name, create a category table"
  [db category]
  (try (j/with-connection db
         (j/create-table category
                         [:url :text]
                         [:content :text]))
       (catch Exception e (println e))))

(defn make-tables
  "creates all the category tables in the database defined as 'db'"
  [db]
  (map #(create-cat-table db %) categories))

(defn all-examples-of-category
  "Given a category name from `categories` and a db, returns the
   tuples as a seq of {:content '...'} maps"
  [db category]
  (j/query db
           (s/select :content category)))