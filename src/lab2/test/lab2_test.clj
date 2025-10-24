(ns lab2.test.lab2-test
  (:require [clojure.test :refer :all] [lab2.lab2-1 :refer :all] [lab2.lab2-2 :refer :all] [lab2.lab2-common :refer :all]))

(deftest check-work-for-area-trapezoid
          (is (= (area-trapezoid parabola 2 7) 132.5))
          (is (= (area-trapezoid parabola -2 2) 16.0))
          (is (= (area-trapezoid linear-function 2 7) 22.5))
          (is (= (area-trapezoid linear-function -2 2) 0.0))
          (is (= (area-trapezoid polynomial-function 2 7) 6462.5))
          (is (= (area-trapezoid polynomial-function -2 2) 112.0)))

(defn compare-two-float
  [f1 f2]
  (<= (Math/abs (float (- f1 f2))) 1e-2))

(deftest check-work-for-integral
  (let [l 0.001
        simple-integral (integral parabola l)
        opt-integral (opt-integral parabola l)
        inf-integral (infinity-integral parabola l)]
    (is (compare-two-float (simple-integral 12) 576.0))
    (is (compare-two-float (simple-integral 9) 243.0))
    (is (compare-two-float (opt-integral 12) 576.0))
    (is (compare-two-float (opt-integral 9) 243.0))
    (is (compare-two-float (inf-integral 12) 576.0))
    (is (compare-two-float (inf-integral 9) 243.0))
    )
  )

(defn time-integral
  [integral values]
  (doall
    (map
      (fn [val]
        (println val)
        (time (integral val)))
      values)
    )
  )

(deftest compare-integral
  (let [l 0.001
        simple-integral (integral linear-function l)
        opt-integral (opt-integral linear-function l)
        inf-integral (infinity-integral linear-function l)
        values (list 30 50 30)]
    (println "Simple integral")
    (time-integral simple-integral values)
    (println)
    (println "Opt integral")
    (time-integral opt-integral values)
    (println)
    (println "Inf integral")
    (time-integral inf-integral values)
    )
  )




