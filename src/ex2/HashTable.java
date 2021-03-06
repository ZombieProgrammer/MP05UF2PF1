package ex2;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

public class HashTable {
    private int INITIAL_SIZE = 16;
    private int size = 0;
    private HashEntry[] entries = new HashEntry[INITIAL_SIZE];

    public int size(){
        return this.size;
    }

    public int real_size(){
        return this.INITIAL_SIZE;
    }

    public void put(String key, String value) {
        int hash = getHash(key);
        final HashEntry hashEntry = new HashEntry(key, value);
        if(entries[hash] == null) {
            entries[hash] = hashEntry;
            this.size++;
        }
        else {
            HashEntry temp = entries[hash];
            boolean found = false;
            do{
                if(temp.key == key){
                    temp.value = value;
                    found = true;
                    break;
                }
                else if(temp.next != null)
                    temp = temp.next;

            }while (temp.next != null || temp.key.equals(key));

            if(!found) {
                temp.next = hashEntry;
                hashEntry.prev = temp;
                this.size++;
            }
        }

    }

    /**
     * Returns 'null' if the element is not found.
     */
    public String get(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {

            HashEntry temp = getHashEntry(key, entries[hash]);

            if (temp.key==key)
                return temp.value;
            else return null;
        }

        return null;
    }

    //REFACCIÓ: He hecho esta refactorización de método ya que se usaba en más de 1 método por lo que creí conveniente extraerlo.
    private HashEntry getHashEntry(String key, HashEntry entry) {
        HashEntry temp = entry;
        while( !temp.key.equals(key) && temp.next != null)
            temp = temp.next;
        return temp;
    }

    public void drop(String key) {
        int hash = getHash(key);
        if(entries[hash] != null) {

            HashEntry temp = getHashEntry(key, entries[hash]);

            if (temp.key!=key) return;
            else{

                //Borrar si es el primero
                if (temp.prev==null) {
                    //Borrar si es el primero
                    entries[hash] = temp.next;             //esborrar element únic (no col·lissió)
                }else if (temp.next==null){
                    //Borrar si es el ultimo
                    temp.prev.next=null;
                }else {
                    //Borrar si está en medio
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                }
                size--;
            }
        }
    }

    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return key.hashCode() % INITIAL_SIZE;
    }

    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    @Override
    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (HashEntry entry : entries) {
            if(entry == null) {
                continue;
            }
            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            HashEntry temp = entry.next;
            while(temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

}