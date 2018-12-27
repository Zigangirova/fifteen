import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class State {
    private Map neighbors = new HashMap<Integer, ArrayList<Integer>>();
    private List<Integer> trace = new ArrayList<Integer>();
    private State parent = null;

    State(Map neighbors){
        this.neighbors = neighbors;
    }
}
