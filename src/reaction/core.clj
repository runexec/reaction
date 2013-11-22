(ns reaction.core)

(def reactions (atom {}))

(defmacro reactive! [-symbol]
  `(swap! ~'reactions 
          assoc
          '~-symbol 
          {:val ~-symbol
           :actions []}))

(defmacro rget [reactive-symbol]
  (let [{:keys [val actions]} (get @reactions reactive-symbol [])]
    (if-not (seq actions)
      val
      (let [v (atom val)]
        (doseq [a actions]
          (swap! v a))
        @v))))
            
(defmacro rapply! [reactive-symbol -fn]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :actions]
            #(conj % ~-fn))
     nil))

(defmacro rset! [reactive-symbol value]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :val]
            (fn [x#] ~value))
     nil))

(defmacro remove-actions! [reactive-symbol]
  `(do 
     (swap! ~'reactions 
            update-in 
            ['~reactive-symbol :actions]
            empty)
     nil))

(deftype Active [symbol]
    clojure.lang.IDeref
  (deref [object] 
    (eval `(rget ~symbol))))

(defmacro def-reactive! [-symbol & [value]]
  `(do (def ~-symbol ~value)
       (reactive! ~-symbol)
       (def ~-symbol (Active. '~-symbol))))

(defmacro original-value [reactive-symbol]
  `(:val
    (get (deref ~'reactions) 
         '~reactive-symbol)))
