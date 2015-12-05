(ns meal.input)


(defn text [id value & {:keys [class placeholder]}]
  [:input {:type :text
           :id id
           :name id
           :placeholder placeholder
           :value @value
           :class class
           :on-change #(reset! value (-> % .-target .-value))}])


(defn textarea [id value & {:keys [class placeholder]}]
  [:textarea {:id id
              :name id
              :placeholder placeholder
              :value @value
              :class class
              :on-change #(reset! value (-> % .-target .-value))}])


(defn file [id value & {:keys [class placeholder accept]}]
  [:input {:type :file
           :id id
           :name id
           :placeholder placeholder
           :value @value
           :class class
           :accept accept
           :on-change #(reset! value (-> % .-target .-value))}])


(defn button [& {:keys [class label on-click]}]
  [:button {:class class
            :on-click on-click}
   label])
