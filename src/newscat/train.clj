(ns newscat.train
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:use [opennlp.nlp]
       ; [opennlp.treebank]
        [opennlp.tools.train]
        [newscat.db]))

; write functions to access raw text, tokenize and clean it up, and
; then compact it. Eventually, create a training file that OpenNLP
; expects to find.

(def tokenizer (make-tokenizer (io/resource "en-token.bin")))

(defn stop-words
  "Function that returns a set of stop words when called"
  []
  (set (string/split-lines
        (slurp (io/resource "stop_words.txt")))))

(defn filter-stop-words
  "Given a function that returns a seq of stop words, filter line for
  stop words"
  [line stop-words-fn]
  (filter #(not (contains? (stop-words-fn) %))
          (set line)))

(defn bag-of-words
  "takes a string, a set of stop words, and a tokenizer and removes
   any stop words, returning a tokenized set of words"
  [str stop-words-fn tokenizer]
  (set (filter-stop-words (tokenizer str)
                          stop-words-fn)))

(defn process-category
  "Given a category name and a db, process every example and return
   a seq of sets of words"
  [db category stop-words-fn tokenizer]
  (->> (map #(bag-of-words (->> % :content) stop-words-fn tokenizer)
            (all-examples-of-category db category))
       (filter #(not= (count %) 0))))

(defn training-example-for
  "Given a category name and a seq containing words that should be trained,
   produce a string that will represent the training example,
   like described in http://opennlp.apache.org/documentation/1.5.3/manual/opennlp.html#tools.doccat.training"
  [category words-seq]
  (string/join " " (cons category words-seq)))

(defn train-for-category
  "Creates a training example for each example for the given category
   name."
  [db category stop-words-fn tokenizer]
  (map #(training-example-for category %)
       (process-category db category stop-words-fn tokenizer)))

(defn produce-model
  "given a seq of categories, produces all training examples of all categories"
  [db categories stop-words-fn tokenizer]
  (flatten (map #(train-for-category db % stop-words-fn tokenizer)
                categories)))

(defn write-training-file
  "write a sequence of strings representing training examples to the given file name"
  ([filename] (write-training-file filename db categories stop-words tokenizer))
  ([filename db categories stop-words-fn tokenizer]
     (->> (produce-model db categories stop-words-fn tokenizer)
          (interpose \newline)
          (reduce str)
          (spit filename))))