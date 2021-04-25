package Domain;

import java.util.Observable;

    public class ObservableType extends Observable {
        private String name;
        private int id;
        private String msg;

        public ObservableType(String name,int id) {
            this.name = name;
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
        public String getMessage() {
            return msg;
        }

        public void sendAll(String msg)
        {
            this.msg = msg;
            setChanged();
            notifyObservers();
        }

    }
