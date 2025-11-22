(ns lab4.test.test (:require [clojure.test :refer :all]
                             [lab4.lab4 :refer :all]))

;получение значения константы
(defn get-constant-val [exp]
  (second exp))

;получение значения переменной
(defn get-variable-val [exp]
  (second exp))

(deftest check-work
  (testing "проверки переменных"
    (is (variable? (variable :v)))
    (is (equals-vars? (variable :v) (variable :v)))
    (is (= false (equals-vars? (variable :v1) (variable :v2))))
    (is (= (get-variable-val (variable :v)) :v)))
  (testing "проверки констант"
    (is (False? (constant 0)))
    (is (True? (constant 1)))
    (is (constant? (constant 1)))
    (is (= 1 (get-constant-val (constant 1)))))
  (testing "проверки конъюнкции"
    (is (&&? (&& (variable :v1) (variable :v2))))
    (is (= (&& (constant 0) (constant 1)) (constant 0)))
    (is (= (&& (&& (variable :v1) (variable :v2)) (&& (variable :v3) (variable :v4))) (&& (variable :v1) (variable :v2) (variable :v3) (variable :v4))))
    (is (= (&& (variable :v1) (|| (variable :v2) (variable :v3))) (|| (&& (variable :v1) (variable :v2)) (&& (variable :v1) (variable :v3))))))
  (testing "проверки дизъюнкции"
    (is (||? (|| (variable :v1) (variable :v2))))
    (is (= (|| (constant 0) (constant 1)) (constant 1))))
    (is (= (|| (|| (variable :v1) (variable :v2)) (|| (variable :v3) (variable :v4))) (|| (variable :v1) (variable :v2) (variable :v3) (variable :v4))))
  (testing "проверки отрицания"
    (is (no? (no (variable :v1))))
    (is (= (no (constant 0)) (constant 1)))
    (is (= (no (|| (variable :v1) (variable :v2))) (&& (no (variable :v1)) (no (variable :v2))))))
    (is (= (no (no (variable :v1))) (variable :v1)))
  (testing "проверки импликация"
    (is (= (--> (variable :v1) (variable :v2)) (|| (no (variable :v1)) (variable :v2)))))
  (testing "проверки xor"
    (is (= (xor-for-two (variable :v1) (variable :v2)) (|| (&& (variable :v1) (no (variable :v2))) (&& (no (variable :v1)) (variable :v2)))))
    (is (= (xor (variable :v1) (variable :v2)) (|| (&& (variable :v1) (no (variable :v2))) (&& (no (variable :v1)) (variable :v2)))))
    (is (= (xor (variable :v1) (variable :v2)) (xor-for-two (variable :v1) (variable :v2)))))
  (testing "проверки стрелка Пирса"
    (is (= (peirce (variable :v1) (variable :v2)) (no (|| (variable :v1) (variable :v2)))))
    (is (= (peirce (variable :v1) (variable :v2) (variable :v3)) (no (|| (variable :v1) (variable :v2) (variable :v3))))))
  (testing "проверки выражений"
    (is (exp? (variable :v1)))
    (is (exp? (constant 1)))
    (is (exp? (&& (variable :v1) (variable :v2))))
    (is (exp? (|| (variable :v1) (variable :v2))))
    (is (exp? (no (variable :v1))))
    (is (exp? (--> (variable :v1) (variable :v2))))
    (is (exp? (xor (variable :v1) (variable :v2))))
    (is (exp? (peirce (variable :v1) (variable :v2))))
    (is (= false (exp? "test"))))
  (testing "проверки замены переменной"(variable :v3)
    (is (= (replace-var (&& (variable :v1) (variable :v2)) (variable :v1) (&& (variable :v3) (variable :v4))) (&& (variable :v3) (variable :v4) (variable :v2))))))