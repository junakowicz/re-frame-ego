(ns lcsim.db)

(def default-db
  {:grid-dimensions {:w 30 :h 40}
   :shapes {:cells  []
            :direction {:x 1 :y 1}}
   :bullets {:cells  []
             :direction {:x 0 :y -1}}
   :ship {:cells  [[4 4]       [6 4]
                   [4 5] [5 5] [6 5]]}
   :score 0
   :lcd-text "NAME"
   :active-panel :welcome})