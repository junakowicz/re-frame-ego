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



