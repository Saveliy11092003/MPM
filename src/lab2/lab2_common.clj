(ns lab2.lab2-common)

(defn polynomial-function [x] (+ (+ (* (* (* x x) x) x) (* 3 (* x x))) x))
(defn linear-function [x] x)
(defn parabola [x] (* x x))


; S = (f(a) + f(b)) * (b - a) / 2
(defn area-trapezoid
  [f a b]
  (* (* (+ (f a) (f b)) (- b a)) 0.5))

(defn area-trapezoid-seg
  [f l i]
  (area-trapezoid f (* l i) (* l (inc i))))

(defn integral
  [f l]
  (fn [x]
    (let [n (Math/floor (/ x l))]
      (reduce
        (fn [acc i] (+ acc (area-trapezoid-seg f l i)))
        (area-trapezoid f (* l n) x)
        (range (dec n) -1 -1))
      )
    )
  )


;(def L (integral linear-function 0.01))
;(println (L 1))
