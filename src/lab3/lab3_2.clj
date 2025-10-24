(ns lab3.lab3-2  (:require [lab3.common :refer :all]))

(println (lazy-parallel-filter [1 2 3 4 5 6 7 8] even? 3 3))

