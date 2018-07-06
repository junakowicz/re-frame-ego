(ns lcsim.utils
(:require
 [lcsim.shapes :as shapes])
)

(defn wrap-bounds [pos max]
  (cond
    (> pos max) 0
    (< pos 0) max
    :else pos))

(defn wrap-move [dimensions xpos ypos]
  (let [xmax (:w dimensions)
        ymax (:h dimensions)
        xnew (map #(wrap-bounds % xmax) xpos)
        ynew (map #(wrap-bounds % ymax) ypos)]
    [xnew ynew]))

(defn destroy-on-bounds [pos max]
  (cond
    (> pos max) nil
    (< pos 0) nil
    :else pos))

(defn destroy-out [dimensions xpos ypos]
  (let [xmax (:w dimensions)
        ymax (:h dimensions)
        xnew (map #(destroy-on-bounds % xmax) xpos)
        ynew (map #(destroy-on-bounds % ymax) ypos)]
    [xnew ynew]))

(defn offset-cells [cells-pos offset-xy]
  (let [cells-x (for [pos cells-pos] (first pos))
        cells-y (for [pos cells-pos] (second pos))
        dx (:x offset-xy)
        dy (:y offset-xy)
        tox (map #(+ dx %) cells-x)
        toy (map #(+ dy %) cells-y)]
    [tox toy]))

(defn offset-cells-to-vec [cells-pos offset-xy]
  "Accepts vector of cell positions in format [[1 1] [2 2]] and offset {:x 5 :y 1}
  Returns [[6 2] [7 3]]"
  (apply map vector (offset-cells cells-pos offset-xy)))


(defn get-shape [l]
  (cond
    (= "A" l) shapes/A
    (= "B" l) shapes/B
    (= "C" l) shapes/C
    (= "D" l) shapes/D
    (= "E" l) shapes/E
    (= "F" l) shapes/F
    (= "G" l) shapes/G
    (= "H" l) shapes/H
    (= "I" l) shapes/I
    (= "J" l) shapes/J
    (= "K" l) shapes/K
    (= "L" l) shapes/L
    (= "M" l) shapes/M
    (= "N" l) shapes/N
    (= "O" l) shapes/O
    (= "P" l) shapes/P
    (= "R" l) shapes/R
    (= "S" l) shapes/S
    (= "T" l) shapes/T
    (= "U" l) shapes/U
    (= "V" l) shapes/V
    (= "W" l) shapes/W
    (= "X" l) shapes/X
    (= "Y" l) shapes/Y
    (= "Z" l) shapes/Z
    (= "1" l) shapes/ONE
    (= "2" l) shapes/TWO
    (= "3" l) shapes/THREE
    (= "4" l) shapes/FOUR
    (= "5" l) shapes/FIVE
    (= "6" l) shapes/SIX
    (= "7" l) shapes/SEVEN
    (= "8" l) shapes/EIGHT
    (= "9" l) shapes/NINE
    (= "0" l) shapes/ZERO
    :else shapes/QUESTION))

(defn cells-from-text [txt]
  (let [grouped (map-indexed (fn [idx itm] (offset-cells-to-vec (get-shape itm) {:x (* idx 5) :y 1})) txt)]

    (apply concat grouped)))