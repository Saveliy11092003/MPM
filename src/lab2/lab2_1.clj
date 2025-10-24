(ns lab2.lab2-1 (:require [ lab2.lab2-common :refer :all]))

(def opt-area-trapezoid-seg
  (memoize area-trapezoid-seg)
  )

(defn opt-integral
  [f l]
  (memoize (fn [x]
    (let [n (Math/floor (/ x l))]
      (reduce
        (fn [acc i] (+ acc (opt-area-trapezoid-seg f l i)))
        (area-trapezoid f (* l n) x)
        (range (dec n) -1 -1))
      ))))


;(def L (opt-integral linear-function 0.01))
;(println (L 1))