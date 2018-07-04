(ns lcsim.db)


(def A [[1 0] [2 0]
[0 1]             [3 1]
[0 2] [1 2] [2 2] [3 2]
[0 3]             [3 3]
[0 4]             [3 4]]) 

(def default-db
  {:name "re-frame"
   :grid-dimensions {:w 10 :h 10}
   :marked-cells A                  
                  
                  
   :direction {:x 1 :y 0}
   })
