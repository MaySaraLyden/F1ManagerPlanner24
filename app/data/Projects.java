package app.data;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class Projects {
    private static Map<String, Integer> designCosts = new LinkedHashMap<>();
    private static Map<String, Integer> researchCosts = new LinkedHashMap<>();
    private static Map<String, Integer> manufactureCosts = new LinkedHashMap<>();
    private static Map<String, Float> approachMultiplier = new LinkedHashMap<>();
    private static Map<String, Float> manufactureApproachMultiplier = new LinkedHashMap<>();
    private Map<Integer, ProjectBlock> projectList = new LinkedHashMap<>();
    private List<Integer> unusedIndices = new ArrayList<>();
    private List<Integer> usedIndices = new ArrayList<>();
    private int nextIndex;
    private int initialCost;
    private int costCap;

    public Projects() {
        nextIndex = 0;

        // Design Costs
        designCosts.put("Chassis", 1000000);
        designCosts.put("Front Wing", 1800000);
        designCosts.put("Rear Wing", 1800000);
        designCosts.put("Sidepod", 1000000);
        designCosts.put("Underfloor", 1000000);
        designCosts.put("Suspension", 1000000);

        // Research Costs
        researchCosts.put("Chassis", 1000000);
        researchCosts.put("Front Wing", 1800000);
        researchCosts.put("Rear Wing", 1800000);
        researchCosts.put("Sidepod", 1000000);
        researchCosts.put("Underfloor", 1000000);
        researchCosts.put("Suspension", 1000000);
        
        // Manufacture Costs
        manufactureCosts.put("Chassis", 550000);
        manufactureCosts.put("Front Wing", 225000);
        manufactureCosts.put("Rear Wing", 275000);
        manufactureCosts.put("Sidepod", 450000);
        manufactureCosts.put("Underfloor", 400000);
        manufactureCosts.put("Suspension", 300000);

        // Design/Research Multiplier
        approachMultiplier.put("Normal", new Float(1.0));
        approachMultiplier.put("Rushed", new Float(1.5));
        approachMultiplier.put("Intense", new Float(3.0));

        // Manufacture Multiplier
        manufactureApproachMultiplier.put("Normal", new Float(1.0));
        manufactureApproachMultiplier.put("Rushed", new Float(1.5));
        manufactureApproachMultiplier.put("Emergency", new Float(4.0));

        // Cost Cap
        costCap = 139_200_000;
    }

    // Block Creation Methods

    public void createDesignBlock(int month) {
        int index = getNextIndex();
        projectList.put(index, new DesignBlock(index, month));
    }

    public void createResearchBlock(int month) {
        int index = getNextIndex();
        projectList.put(index, new ResearchBlock(index, month));
    }

    public void createManufactureBlock(int month) {
        int index = getNextIndex();
        projectList.put(index, new ManufactureBlock(index, month));
    }

    public void createDesignBlock(int month, String part, String approach) {
        int index = getNextIndex();
        projectList.put(index, new DesignBlock(index, month, part, approach));
    }

    public void createResearchBlock(int month, String part, String approach) {
        int index = getNextIndex();
        projectList.put(index, new ResearchBlock(index, month, part, approach));
    }

    public void createManufactureBlock(int month, String part, String approach, int quantity) {
        int index = getNextIndex();
        projectList.put(index, new ManufactureBlock(index, month, part, approach, quantity));
    }

    // Block Deletion Methods

    public void deleteProjectBlock(int index) {
        if (projectList.remove(index) != null) {
            usedIndices.remove(Integer.valueOf(index));
            if (!unusedIndices.contains(index)) {
                unusedIndices.add(index);
                Collections.sort(unusedIndices);
            }
        }
    }

    // Index Mangement

    public void updateNextIndex() {
        if (!unusedIndices.isEmpty()) {
            Collections.sort(unusedIndices);
            nextIndex = unusedIndices.get(0);
        } else if(!usedIndices.isEmpty()) {
            nextIndex = Collections.max(usedIndices) + 1;
        } else {
            nextIndex = 0;
        }
    }

    private int getNextIndex() {
        if (!unusedIndices.isEmpty()) {
            int index = unusedIndices.remove(0);
            usedIndices.add(index);
            return index;
        }
    
        int index = nextIndex;
        usedIndices.add(index);
        nextIndex++;
        return index;
    }

    // Block Update/Modify Methods

    public void updateDesignBlock(int index, int month, String part, String approach) {
        if(usedIndices.contains(index)){
            if(projectList.get(index) instanceof DesignBlock){
                DesignBlock thisProject = (DesignBlock) projectList.get(index);
                thisProject.setPart(part);
                thisProject.setApproach(approach);
                thisProject.setMonth(month);
            } else {
                System.out.println("Not a Design Block");
            }
        } else {
                DesignBlock newProject = new DesignBlock(index, month, part, approach);
                projectList.put(index, newProject);
                usedIndices.add(index);
        }
    }

    public void updateResearchBlock(int index, int month, String part, String approach) {
        if(usedIndices.contains(index)){
            if(projectList.get(index) instanceof ResearchBlock){
                ResearchBlock thisProject = (ResearchBlock) projectList.get(index);
                thisProject.setPart(part);
                thisProject.setApproach(approach);
                thisProject.setMonth(month);
            } else {
                System.out.println("Not a Research Block");
            }
        } else {
                ResearchBlock newProject = new ResearchBlock(index, month, part, approach);
                projectList.put(index, newProject);
                usedIndices.add(index);
        }
    }

    public void updateManufactureBlock(int index, int month, String part, String approach, int quantity) {
        if(usedIndices.contains(index)){
            if(projectList.get(index) instanceof ManufactureBlock){
                ManufactureBlock thisProject = (ManufactureBlock) projectList.get(index);
                thisProject.setPart(part);
                thisProject.setApproach(approach);
                thisProject.setMonth(month);
                thisProject.setQuantity(quantity);
            } else {
                System.out.println("Not a Manufacture Block");
            }
        } else {
            ManufactureBlock newProject = new ManufactureBlock(index, month, part, approach, quantity);
            projectList.put(index, newProject);
            usedIndices.add(index);
        }
    }

    public void moveBlock(int index, int newMonth) {
        if(projectList.containsKey(index)){
            projectList.get(index).setMonth(newMonth);
        } else {
            System.out.println("Project does not exist");
        }
    }

    public void changePart(int index, String newPart) {
        if(projectList.containsKey(index)){
            projectList.get(index).setPart(newPart);
        } else {
            System.out.println("Project does not exist");
        }
    }

    public void changeApproach(int index, String newApproach) {
        if(projectList.containsKey(index)){
            projectList.get(index).setApproach(newApproach);
        } else {
            System.out.println("Project does not exist");
        }
    }

    public void changeQuantity(int index, int newQuantity) {
        if(projectList.containsKey(index)){
            if (projectList.get(index) instanceof ManufactureBlock){
                ManufactureBlock thisProject = (ManufactureBlock) projectList.get(index);
                thisProject.setQuantity(newQuantity);
            }
        } else {
            System.out.println("Project does not exist");
        }
    }

    public void changeCompleted(int index, boolean isComplete) {
        if(projectList.containsKey(index)){
            projectList.get(index).setComplete(isComplete);
        } else {
            System.out.println("Project does not exist");
        }
    }

    // Setters

    public void setInitialCost(int initialCost) {
        this.initialCost = initialCost;
    }
    
    public void setCostCap(int costCap) {
        this.costCap = costCap;
    }

    // Getters

    public static Map<String, Integer> getDesignCosts() {
        return designCosts;
    }

    public static Map<String, Integer> getResearchCosts() {
        return researchCosts;
    }

    public static Map<String, Integer> getManufactureCosts() {
        return manufactureCosts;
    }

    public static Map<String, Float> getApproachMultiplier() {
        return approachMultiplier;
    }

    public static Map<String, Float> getManufactureApproachMultiplier() {
        return manufactureApproachMultiplier;
    }

    public Map<Integer, ProjectBlock> getProjects() {
        return projectList;
    }

    public ProjectBlock getProjectBlock(int index) {
            return projectList.get(index);
    }

    public int getIntialCost() {
        return initialCost;
    }

    public int getCostCap() {
        return costCap;
    }

    public List<ProjectBlock> getBlocksForMonth(int month) {
        List<ProjectBlock> returnList = new ArrayList<>();
        Iterator<ProjectBlock> iterator = projectList.values().iterator();
        while(iterator.hasNext()) {
            ProjectBlock thisProject = iterator.next();
            if(thisProject.getMonth() == month){
                returnList.add(thisProject);
                //thisProject.printProject();
            }
        }
        return returnList;
    }

    public int getCostForMonth(int month) {
        List<ProjectBlock> monthList = getBlocksForMonth(month);
        Iterator<ProjectBlock> iterator = monthList.iterator();
        int total = 0;
        while(iterator.hasNext()){
            total += iterator.next().getCost();
        }
        return total;
    }

    public int getTotalCost() {
        Iterator<ProjectBlock> iterator = projectList.values().iterator();
        int total = initialCost;
        while(iterator.hasNext()){
            total += iterator.next().getCost();
        }
        return total;
    }

    public int getCompletedCost() {
        Iterator<ProjectBlock> iterator = projectList.values().iterator();
        int total = initialCost;
        while(iterator.hasNext()){
            ProjectBlock thisProject = iterator.next();
            if(thisProject.isComplete()){
                total += thisProject.getCost();
            }
        }
        return total;
    }

    public int getCostByType(String projectType) {
        Iterator<ProjectBlock> iterator = projectList.values().iterator();
        int total = 0;
        while(iterator.hasNext()){
            ProjectBlock thisProject = iterator.next();
            if(thisProject.getProjectType() == projectType) {
                total += thisProject.getCost();
            }
        }
        return total;
    }

    // Print
    
    public void printAllProjects() {
        System.out.println("Projects List");
        System.out.println("------------");
        Iterator<ProjectBlock> iterator = projectList.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().printProject();
            System.out.println("-----------");
        }
        System.out.println("Initial Cost: " + initialCost);
        System.out.println("Cost Cap: " + costCap);
    }

    // Save & Load

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.print(costCap);
            writer.println();
            writer.print(initialCost);
            writer.println();
            for (ProjectBlock project: projectList.values()){
                writer.print(project.getIndex());
                writer.print("|");
                writer.print(project.getProjectType());
                writer.print("|");
                writer.print(project.getPart());
                writer.print("|");
                writer.print(project.getApproach());
                writer.print("|");
                writer.print(project.getMonth());
                writer.print("|");
                writer.print(project.isComplete());
                if (project instanceof ManufactureBlock) {
                    ManufactureBlock manufacture = (ManufactureBlock) project;
                    writer.print("|");
                    writer.print(manufacture.getQuantity());
                }
                writer.println();
            }
        }

    }

    public void loadFromFile(String filename) throws IOException {
        projectList.clear();
        usedIndices.clear();
        unusedIndices.clear();
        nextIndex=0;

        try(BufferedReader reader =
            new BufferedReader(new FileReader(filename))) {
                String line;
                int costCap = Integer.parseInt(reader.readLine());
                setCostCap(costCap);
                int initialCost = Integer.parseInt(reader.readLine());
                setInitialCost(initialCost);
                while((line = reader.readLine()) != null) {
                    String[] data = line.split("\\|");
                    int index = Integer.parseInt(data[0]);
                    String type = data[1];
                    String part = data[2];
                    String approach = data[3];
                    int month = Integer.parseInt(data[4]);
                    boolean complete = Boolean.parseBoolean(data[5]);

                    if("Design".equals(type)) {
                        updateDesignBlock(index, month, part, approach);

                    } else if("Research".equals(type)) {
                        updateResearchBlock(index, month, part, approach);

                    } else if("Manufacture".equals(type)) {
                        int quantity = Integer.parseInt(data[6]);
                        updateManufactureBlock(index, month, part, approach, quantity);

                    }

                    changeCompleted(index, complete);
                }
            }
        
        updateNextIndex();
        printAllProjects();
    }
}
