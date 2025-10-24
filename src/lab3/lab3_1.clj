(ns lab3.lab3_1
  (:require [lab3.common :refer :all]))

(println (parallel-filter [1 2 3 4 5 6 7 8] even? 3))