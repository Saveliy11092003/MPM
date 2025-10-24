(ns lab1.lab1_2)

(defn add-symbol-with-check
  [word symbol]
  (if (= (str (last word)) symbol)
    nil
    (str word symbol)))

;(println (add-symbol-with-check "ab" "c"))

(defn get-comb-for-word
  [word symbols acc]
  (if (empty? symbols)
    acc
    (let [modified-word (add-symbol-with-check word (first symbols))
          modified-symbols (rest symbols)]
      (if (nil? modified-word)
        (recur word modified-symbols acc)
        (recur word modified-symbols (into acc (list modified-word)))
        )
      )
    )
  )

;(println (get-comb-for-word "ab" ["a" "b" "c"] []))

(defn get-comb-for-words
  [words symbols acc]
  (if (empty? words)
    acc
    (recur (rest words) symbols (into acc (get-comb-for-word (first words) symbols [])))
    )
  )

;(println (get-comb-for-words ["a" "b"] ["a" "b" "c"] []))

(defn get-all-comb
  [acc symbols n]
  (if (empty? symbols)
    nil
    (if (empty? acc)
      (recur symbols symbols n)
      (if (= (count (first acc)) n)
        acc
        (recur (get-comb-for-words acc symbols []) symbols n)
        )
      )
    )
  )

(println (get-all-comb [] ["a" "b" "c"] 2))