(ns newscat.train
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:use [opennlp.nlp]
       ; [opennlp.treebank]
        [opennlp.tools.train]))

; write functions to access raw text, tokenize and clean it up, and
; then compact it. Eventually, create a training file that OpenNLP
; expects to find.

(def tokenizer (make-tokenizer (io/resource "en-token.bin")))

(defn stop-words
  "Set of stop words"
  []
  (set (string/split-lines
        (slurp (io/resource "stop_words.txt")))))

(defn filter-stop-words [line stop-words-fn]
  (filter #(not (contains? (stop-words-fn) %))
          (set line)))

(defn bag-of-words
  "takes a string, a set of stop words, and a tokenizer and removes
   any stop words, returning a tokenized set of words"
  [str stop-words-fn tokenizer]
  (set (filter-stop-words (tokenizer str)
                          stop-words-fn)))
