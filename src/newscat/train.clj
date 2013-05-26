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
  (map #(bag-of-words (->> % :content) stop-words-fn tokenizer)
       (all-examples-of-category db category)))