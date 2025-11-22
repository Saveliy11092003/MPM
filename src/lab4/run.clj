(ns lab4.run (:require [lab4.lab4 :refer :all]))

(println (convert-string (replace-var (&& (variable :v1) (variable :v2)) (variable :v1) (|| (variable :v3) (variable :v4) (variable :v5)))))
(println (convert-string (replace-var (|| (variable :v3) (xor (variable :v1) (variable :v2))) (variable :v2) (variable :v4))))
(println (convert-string (replace-var (|| (variable :v3) (peirce (variable :v1) (variable :v2))) (variable :v2) (variable :v4))))