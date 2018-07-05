(ns lcsim.utils)

(defn check-bounds [pos max]
  (cond
    (> pos max) 0
    (< pos 0) max
    :else pos))

(defn wrap-move [dimensions xpos ypos]
  (let [xmax (:w dimensions)
        ymax (:h dimensions)
        xnew (map #(check-bounds % xmax) xpos)
        ynew (map #(check-bounds % ymax) ypos)]
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

