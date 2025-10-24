(ns lab1.lab1-4
  (:require [lab1.lab1-3 :refer :all]))

(defn add-symbol-with-check
  [word symbol]
  (if (= (str (last word)) symbol)
    nil
    (str word symbol)))

(defn get-comb-for-word
  [word symbols]
  (my-filter some? (my-map #(add-symbol-with-check word %1) symbols)))

;(println (get-comb-for-word "ab" ["a" "b" "c"]))

(defn get-comb-for-words
  [words symbols]
  (reduce #(into %1 (get-comb-for-word %2 symbols)) [] words))

;(println (get-comb-for-words ["ab" "ca"] ["a" "b" "c"]))

(defn get-all-comb
  [symbols n]
  (if (empty? symbols)
    []
    (reduce (
              fn [acc item]
              (if (empty? acc)
                symbols
                (get-comb-for-words acc symbols))
              )
            []
            (range 0 n)
            )
    )
  )

(println (get-all-comb ["a" "b" "c"] 2))