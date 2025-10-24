(ns lab3.common)

(defn collection-distribution
  [collection length extra-count]
  (let [section (map first
                     (iterate
                       (fn [[sec [remainder i]]]
                         (if (> extra-count i)
                           [(take (inc length) remainder) [(drop (inc length) remainder) (inc i)]]
                           [(take length remainder) [(drop length remainder) (inc i)]])
                         )
                       [collection [collection 0]]))]
    (fn [index] (nth section index))
    )
  )

(defn parallel-filter
  [collection pred? thread-count]
  (let [size (count collection)
        length (quot size thread-count)
        remainder (mod size thread-count)]
    (->>
      (iterate inc 1)
      (take thread-count)
      (map #(future
              (doall
                (filter pred?
                        ((collection-distribution collection length remainder) %)
                        )
                )
              )
           )
      (doall)
      (mapcat deref)
      )
    )
  )

(defn lazy-parallel-filter
  [collection pred? length thread-count]
  (lazy-seq
    (concat
      (parallel-filter (take length collection) pred? thread-count)
      (if (empty? collection)
        []
        (lazy-parallel-filter (drop length collection) pred? length thread-count)
        )
      )
    )
  )
