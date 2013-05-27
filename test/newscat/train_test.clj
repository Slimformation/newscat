(ns newscat.train-test
  (:use clojure.test
        newscat.train))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest simple-bag-of-words
  (testing "whether bag-of-words works for trivial cases"
    (is (= (bag-of-words "I went to the store."
                         stop-words
                         tokenizer)
           #{"store" "I"}))))

(deftest training-example-for-test
  (testing "Whether a simple case works"
    (is (= (training-example-for "politics" #{"government" "world"})
           "politics government world"))))