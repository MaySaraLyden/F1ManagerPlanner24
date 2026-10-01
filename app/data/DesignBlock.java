package app.data;
import java.util.Map;

public class DesignBlock extends ProjectBlock {

    public DesignBlock(int index, int month){
        super(index, month);
        setProjectType("Design");
        calcCost();
    }

    public DesignBlock(int index, int month, String part, String approach){
        super(index, month, "Design", part, approach);
        calcCost();
    }
    
    // Cost Calculation

    @Override
    public void calcCost(){
        Map<String, Integer> designCosts = Projects.getDesignCosts();
        Map<String, Float> approachMultiplier = Projects.getApproachMultiplier();

        int partCost = designCosts.get(getPart());
        float approachMult = approachMultiplier.get(getApproach());
        float calcCost = partCost * approachMult;
        setCost((int) Math.ceil(calcCost));
    }

    // Print Methods
    
    @Override protected void printProjectTitle() {
        System.out.println(getIndex() + " Design Project");
    }
}
