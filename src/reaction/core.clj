(ns reaction.core
  (:refer-clojure :exclude [pop!]))

(def reactions (atom {}))

(defmacro reactive! 
  "Needed for def-reactive! and applies assoc to reactions atom"
  [-symbol]
  `(swap! ~'reactions 
          assoc
          '~-symbol 
          {:val ~-symbol
           :actions []}))

(defmacro rget 
  "Applies action-coll fns to the original value of reactive binding"
  [reactive-symbol]
  (let [{:keys [val actions]} (get @reactions reactive-symbol [])]
    (if-not (seq actions)
      val
      (let [v (atom val)]
        (doseq [a actions]
          (swap! v a))
        @v))))

(defmacro rapply! 
  "Sets the value of a reactive binding"
  [reactive-symbol -fn]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :actions]
            #(conj % ~-fn))
     nil))

(defmacro rset!
  "Sets the value of a reactive binding"
  [reactive-symbol value]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :val]
            (fn [x#] ~value))
     nil))

(defmacro remove-actions! 
  "Remove actions from a reactive binding"
  [reactive-symbol]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :actions]
            empty)
     nil))

(deftype Active [symbol]
  ;; "Ensures the changes are applied to the original value"  
  clojure.lang.IDeref
  (deref [object] 
    (eval `(rget ~symbol))))

(defmacro def-reactive! 
  "Binds a reactive value to a symbol"
  [-symbol & [value]]
  `(do (def ~-symbol ~value)
       (reactive! ~-symbol)
       (def ~-symbol (Active. '~-symbol))))

(defmacro original-value 
  "Returns the original value of a reactive binding"
  [reactive-symbol]
  `(:val
    (get (deref ~'reactions) 
         '~reactive-symbol)))

(defmacro push! 
  "calls rapply! for each action fn"
  [reactive-symbol & action-fns]
  `(doseq [fn# ~(vec action-fns)]
     (rapply! ~reactive-symbol fn#)))

(defmacro pop!
  "Pops lasts action fn from action stack. Does n times if provided"
  [reactive-symbol & [n]]
  `(dotimes [i# (or ~n 1)]
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :actions]
            butlast)
     nil))
  

  
