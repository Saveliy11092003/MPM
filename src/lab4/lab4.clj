(ns lab4.lab4
  (:require [clojure.string :as string]))

(defn || [exp & other])
(defn exp? [exp])

;создание переменной
(defn variable [name]
  (if (keyword? name)
    (list ::variable name)
    (throw (IllegalArgumentException. "Variable not keywords"))
    )
  )

;проверка явлется ли переменной
(defn variable? [exp]
  (= (first exp) ::variable)
  )

;создание константы по значению 0 или 1
(defn constant [value]
  (if (or (= 1 value) (= 0 value))
    (list ::constant value)
    (throw (IllegalArgumentException. "Constant value must be 0 or 1")))
  )

;проверка константа ли
(defn constant? [exp]
  (= (first exp) ::constant)
  )

;проверка на ложную константу
(defn False? [exp]
  (and (constant? exp) (= (second exp) 0))
  )

;проверка на истинную константу
(defn True? [exp]
  (and (constant? exp) (= (second exp) 1))
  )

;проверка на равенство переменных
(defn equals-vars? [var1 var2]
  (and
    (variable? var1) (variable? var2)
    (= (second var1) (second var2))
    )
  )

;получение комбинаций
(defn comb [groups]
  (if (empty? groups)
    [[]]
    (for [first-element (first groups) another (comb (rest groups))]
      (cons first-element another)
      )
    )
  )

;получение аргумента
(defn get-args [exp]
  (drop 1 exp)
  )

;проверка является ли выражение дизъюнкцией
(defn ||? [exp]
  (and
    (= (first exp) ::||)
    (every? exp? (get-args exp)))
  )

;проверка является ли выражение конъюнкцией
(defn &&? [exp]
  (and
    (= (first exp) ::&&)
    (every? exp? (get-args exp))
    )
  )

;проверка является ли отрицанием
(defn no? [exp]
  (and
    (= (first exp) ::no)
    (exp? (second exp))
    )
  )

;преобразование в ДНФ
(defn create-dis [exps]
  (apply || (map #(if (> (count %) 1) (cons ::&& %) %)
              (comb (map #(if (||? %) (get-args %) (list %)) exps))
              )
         )
  )

;конъюнкция с упрощениями на месте
(defn && [exp1 & exps]
  (if (and (exp? exp1) (every? exp? exps))
    (let [non-true-exps (filter #(not (True? %)) (cons exp1 exps))]
      (if (> (count non-true-exps) 1)
        (create-dis (reduce #(concat %1 (if (&&? %2) (get-args %2) (list %2))) [] non-true-exps))
        (if (= (count non-true-exps) 1)
          (first non-true-exps)
          (constant 1)
          )
        )
      )
    (throw (IllegalArgumentException. "No exprs"))
    )
  )

;дизъюнкция с упрощениями на месте
(defn || [exp1 & exps]
  (if (and (exp? exp1) (every? exp? exps))
    (let [non-false-exps (filter #(not (False? %)) (cons exp1 exps))]
      (if (> (count non-false-exps) 1)
        (cons ::|| (reduce #(concat %1 (if (||? %2) (get-args %2) (list %2))) [] non-false-exps))
        (if (= (count non-false-exps) 1)
          (first non-false-exps)
          (constant 0))
        )
      )
    (throw (IllegalArgumentException. "No exprs"))
    )
  )

;отрицание
(defn no [exp]
  (if (exp? exp)
    (if (False? exp)
      (constant 1)
      (if (True? exp)
        (constant 0)
        (if (&&? exp)
          (apply || (map #(no %) (get-args exp)))
          (if (||? exp)
            (apply && (map #(no %) (get-args exp)))
            (if (no? exp)
              (second exp)
              (list ::no exp))))))
    (throw (IllegalArgumentException. "No exprs"))
    )
  )

;импликация
(defn --> [exp1 exp2]
  (if (and (exp? exp1) (exp? exp2))
  (|| (no exp1) exp2)
  (throw (IllegalArgumentException. "No exprs"))
  )
  )

;xor для двух выражений
(defn xor-for-two [exp1 exp2]
  (|| (&& exp1 (no exp2)) (&& (no exp1) exp2)))

;xor для нескольких выражений
(defn xor [exp1 & exps]
  (if (and (exp? exp1) (every? exp? exps))
    (reduce xor-for-two exp1 exps)
    (throw (IllegalArgumentException. "No exprs"))
    )
  )

;стрелка Пирса для нескольких выражений
(defn peirce [exp1 & exps]
  (if (and (exp? exp1) (every? exp? exps))
    (no (apply || (cons exp1 exps)))
    (throw (IllegalArgumentException. "No exprs")))
  )

;проверка является ли выражением
(defn exp? [exp]
  (and (coll? exp) (or (constant? exp) (variable? exp) (&&? exp) (||? exp) (no? exp)))
  )

;замена переменной
(defn replace-var [exp var val]
  (if (and (exp? exp) (variable? var) (exp? val))
    (if (variable? exp)
      (if (equals-vars? var exp)
        val
        exp
        )
        (if (constant? exp)
          exp
          (if (&&? exp)
            (apply && (map #(replace-var % var val) (get-args exp)))
            (if (||? exp)
              (apply || (map #(replace-var % var val) (get-args exp)))
              (if (no? exp)
                (no (replace-var (second exp) var val))
                (throw (IllegalArgumentException. "Unknown expression type")))))))
    (throw (IllegalArgumentException. "Invalid replacement format"))))


;конвертируем полученное выражение в строку для читаемоего вида
(defn convert-string [exp]
  (if (exp? exp)
    (if (or (variable? exp) (constant? exp))
      (name (first (get-args exp)))
      (if (&&? exp)
        (apply str ["(" (string/join " & " (map convert-string (get-args exp))) ")"])
        (if (||? exp)
          (apply str ["(" (string/join " | " (map convert-string (get-args exp))) ")"])
          (if (no? exp)
            (apply str ["!" (first (map convert-string (get-args exp)))])
            (throw (IllegalArgumentException. "No exprs"))))))
    (throw (IllegalArgumentException. "Not an expression"))))