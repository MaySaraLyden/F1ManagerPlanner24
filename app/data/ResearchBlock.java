package app.data;
import java.util.Map;

public class ResearchBlock extends ProjectBlock {

    public ResearchBlock(int index, int month){
        super(index, month);
        setProjectType("Research");
        calcCost();
    }
    
    public ResearchBlock(int index, int month, String part, String approach){
        super(index, month, "Research", part, approach);
        calcCost();
    }
    
    // Cost Calculation

    @Override
    public void calcCost(){
        Map<String, Integer> researchCosts = Projects.getResearchCosts();
        Map<String, Float> approachMultiplier = Projects.getApproachMultiplier();

        int partCost = researchCosts.get(getPart());
        float approachMult = approachMultiplier.get(getApproach());
        float calcCost = partCost * approachMult;
        setCost((int) Math.ceil(calcCost));
    }

    // Print Methods
    
    @Override protected void printProjectTitle() {
        System.out.println(getIndex() + " Research Project");
    }
}
