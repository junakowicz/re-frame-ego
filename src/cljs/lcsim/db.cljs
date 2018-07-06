(ns lcsim.db)

(def default-db
  {:name "re-frame"
   :grid-dimensions {:w 70 :h 10}
   :shapes {:cells  [[1 1]]
            :direction {:x 1 :y -1}}
   :bullets {:cells  [[3 3]]
             :direction {:x 1 :y 1}}
   :lcd-text "BLA"})