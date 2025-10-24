(ns lab1.lab1-3)

(defn apply-and-conj [func]
  (fn [acc element]
    (conj acc (func element))))

(defn my-map
  [func collection]
  (seq (reduce (apply-and-conj func) [] collection)))

(defn check-item [pred]
  (fn [acc item]
    (if (pred item)
      (conj acc item)
      acc)))

(defn my-filter
  [pred collection]
  (seq (reduce (check-item pred) [] collection)))

(println (my-map inc [1 2 3 4 5]))
;(println (my-filter even? [1 2 3 4 5 6 7 8 9 10]))