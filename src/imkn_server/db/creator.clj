(ns imkn-server.db.creator
  (:require [clojure.java.jdbc :as sql]
            [korma.db :as kdb]
            [korma.core :as kc]
            [clojure.string :as str])
  (:use
    [korma.db]
    [imkn-server.db.utils]
    [clojure.tools.logging :only [info]]))

(defn- create-news-table-query []
  (sql/create-table-ddl :news
                        [[:id "bigint primary key auto_increment"]
                         [:title "varchar"]
                         [:text "longvarchar"]
                         [:date "timestamp default CURRENT_TIMESTAMP()"]]))

(defn- create-comments-table-query []
  (sql/create-table-ddl :comments
                        [[:id "bigint primary key auto_increment"]
                         [:user "varchar"]
                         [:text "longvarchar"]
                         [:news_id "bigint references news (id)"]
                         [:date "timestamp default CURRENT_TIMESTAMP()"]]))

(defn- create-news-item []
  (sql/insert! db-spec :news
               {:title "Власти установили личность устроившего теракт в клубе в Стамбуле"
                :text  "Глава МИД Турции Мевлют ​Чавушоглу заявил, что власти страны установили личность человека, который устроил теракт в ночном клубе Стамбула в новогоднюю ночь. Об этом он заявил на брифинге, передает агентство Anadolu. При этом министр отказался назвать имя злоумышленника. Ранее агентство сообщило о задержании в Измире еще пятерых подозреваемых, причастных к атаке. Еще восемь человек, подозреваемых в причастности к преступлению, были задержаны 2 января. Власти заявили, что число подозреваемых может возрасти, пока идет следствие по делу о теракте."}))

(defn create-tables []
  (when-not (exists? db-spec "news")
    (sql/execute! db-spec (create-news-table-query)))
  (when-not (exists? db-spec "comments")
    (sql/execute! db-spec (create-comments-table-query)))
  nil)