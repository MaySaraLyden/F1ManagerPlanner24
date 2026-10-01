package app.data;
import java.util.Map;

public class ManufactureBlock extends ProjectBlock {
    private int quantity;

    public ManufactureBlock(int index, int month){
        super(index, month);
        setProjectType("Manufacture");
        setQuantity(1);
        calcCost();
    }

    public ManufactureBlock(int index, int month, String part, String approach, int quantity){
        super(index, month, "Manufacture", part, approach);
        setQuantity(quantity);
        calcCost();
    }

    // Setters

    public void setQuantity(int quantity){
        this.quantity = quantity;
        calcCost();
    }

    // Getters

    public int getQuantity(){
        return quantity;
    }

    // Cost Calculation

    @Override
    public void calcCost() {
        Map<String, Integer> manufactureCosts = Projects.getManufactureCosts();
        Map<String, Float> approachMultiplier = Projects.getManufactureApproachMultiplier();
        int partCost = manufactureCosts.get(getPart());
        float approachMult = approachMultiplier.get(getApproach());
        float calcCost = partCost * approachMult * quantity;
        setCost((int) Math.ceil(calcCost));
    }
    
    // Print Methods
    
    @Override protected void printProjectTitle() {
        System.out.println(getIndex() + " Manufacture Project");
    }
}
