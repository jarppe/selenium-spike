(ns front.main
  (:require [clojure.string :as str]
            [reagent.core :as r]
            [re-frame.core :as rf]))

(rf/reg-sub
  :main-view
  (fn [db _]
    (-> db :main-view)))

;;
;; Fields:
;;

;; Username:

(defn set-username [e]
  (rf/dispatch [:username (-> e .-target .-value)]))

(rf/reg-event-db
  :username
  (rf/path :main-view)
  (fn [db [_ value]]
    (assoc db :username value)))

;; Password:

(defn set-password [e]
  (rf/dispatch [:password (-> e .-target .-value)]))

(rf/reg-event-db
  :password
  (rf/path :main-view)
  (fn [db [_ value]]
    (assoc db :password value)))

;; Sumbit:

(defn login! [e]
  (.preventDefault e)
  (rf/dispatch [:login]))

(defn do-login [username password]
  (rf/dispatch [:login-status
                (if (and (= username "foo")
                         (= password "bar"))
                  :success
                  :fail)]))

(rf/reg-event-db
  :login-status
  (rf/path :main-view)
  (fn [db [_ status]]
    (assoc db :status status)))

(rf/reg-event-fx
  :login
  (rf/path :main-view)
  (fn [{:keys [db]} _]
    (let [username (-> db :username)
          password (-> db :password)
          duration (if (= password "bar")
                     5000
                     1000)]
      (js/setTimeout do-login duration username password))
    (rf/dispatch [:login-status :pending])
    nil))

;;
;; Views:
;;

(defn caption [_]
  [:div.jumbotron
   [:h1.display-3 "Functional testing demo"]
   [:p.lead "Sample application for functional testing demo"]])

(defn login-form [{:keys [username password status]}]
  (let [pending? (= :pending status)]
    [:div.container
     [:form.login-form
      [:div.form-group
       [:label {:for 'username} "Username:"]
       [:input#username.form-control {:type 'email
                                      :placeholder "your.name@address.com"
                                      :default-value username
                                      :on-change set-username
                                      :disabled pending?
                                      :auto-focus true
                                      :data-test-id "username"}]]
      [:div.form-group
       [:label {:for 'password} "Password:"]
       [:input#password.form-control {:type 'password
                                      :value password
                                      :on-change set-password
                                      :disabled pending?
                                      :data-test-id "password"}]]
      [:button.btn.btn-primary {:type 'submit
                                :on-click login!
                                :disabled (or pending? (str/blank? username) (str/blank? password))
                                :data-test-id "submit"}
       "Login"]]]))

(defn login-result [{:keys [status]}]
  [:div.container.login-result
   (condp = status
     :pending [:div.alert.alert-secondary [:i.fa.fa-spinner.fa-spin] "Login in progress, please wait..."]
     :success [:div.alert.alert-success
               {:data-test-id "success"}
               [:i.fa.fa-thumbs-up] "Login successful"]
     :fail [:div.alert.alert-danger [:i.fa.fa-exclamation] "Login failed"]
     nil)])

(defn main-view []
  (let [state @(rf/subscribe [:main-view])]
    [:div
     [caption state]
     [login-form state]
     [login-result state]]))

;;
;; Init:
;;

(r/render [main-view] (js/document.getElementById "app"))
