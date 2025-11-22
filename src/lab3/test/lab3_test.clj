(ns lab3.test.lab3-test (:require [clojure.test :refer :all] [lab3.common :refer :all]))

(defn predicate
  [x]
  (Thread/sleep 200)
  (if (odd? x)
    true
    false
    )
  )

(deftest compare-filters
         (println "Simple filter")
         (time (println (filter predicate (range 100))))
         (println)
         (println "Parallel filter with one thread")
         (time (println (parallel-filter (range 100) predicate 1)))
         (println)
         (println "Parallel filter with two threads")
         (time (println (parallel-filter (range 100) predicate 2)))
         (println)
         (println "Parallel filter with three threads")
         (time (println (parallel-filter (range 100) predicate 3)))
         )

(deftest check-filter
  (is (= (filter predicate (range 30)) (parallel-filter (range 30) predicate 4))))

(deftest compare-with-lazy-filter
  (println "Simple filter")
  (time (println (filter predicate (range 100))))
  (println)
  (println "Lazy parallel filter with one thread")
  (time (println (lazy-parallel-filter (range 100) predicate 20 1)))
  (println)
  (println "Lazy parallel filter with two threads")
  (time (println (lazy-parallel-filter (range 100) predicate 20 2)))
  (println)
  (println "Lazy parallel filter with three threads")
  (time (println (lazy-parallel-filter (range 100) predicate 20 3)))
  )

(deftest check-filter
  (is (= (filter predicate (range 30)) (lazy-parallel-filter (range 30) predicate 7 4))))


(deftest check-lazy
  (println "Lazy parallel filter with two threads with 1000")
  (time (take 1000 (lazy-parallel-filter (range 10000) even? 1000 2)))
  (println "Lazy parallel filter with two threads with 10")
  (time (take 10 (lazy-parallel-filter (range 10000) even? 1000 2)))

  (println "Filter without lazy, with two threads with take")
  (time (take 10 (parallel-filter (range 10000) even? 2)))

  (println "Infinity, lazy filter with two threads with take")
  (time (take 10 (lazy-parallel-filter (iterate inc 1) even? 1000 2)))

  )