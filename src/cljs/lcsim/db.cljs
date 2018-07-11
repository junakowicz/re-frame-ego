(ns lcsim.db)

(def default-db
  {:grid-dimensions {:w 34 :h 40}
   :shapes {:cells  []
            :direction {:x 1 :y 1}}
   :bullets {:cells  []
             :direction {:x 0 :y -1}}
   :ship {:cells  [[16 37] [18 37] [16 38] [17 38] [18 38]]}
   :score 100
   :lcd-text "NAME"
   :active-panel :welcome})