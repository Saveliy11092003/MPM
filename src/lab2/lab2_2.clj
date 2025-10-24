(ns lab2.lab2-2 (:require [ lab2.lab2-common :refer :all]))

(defn infinity-integral [f l]
  (let [seq (map first (iterate (fn [[step_sum i]]
                                  [(+ step_sum (area-trapezoid f (* l (- i 1)) (* l i))) (inc i)]) [0 1]))]
    (fn [x]
      (let [i (Math/floor (/ x l))] (+
                                  (nth seq i)
                                  (area-trapezoid f (* l i) x)))
      )
    )
  )

;(def L (infinity-integral linear-function 0.01))
;(println (L 1))