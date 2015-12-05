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
