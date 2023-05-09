package it.polito.tdp.extflightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		model.creaGrafo(2000);
		System.out.println("NUMERO: "+model.getNumArchi()+", "+model.getNumVertici());
		

		for(Edge ee: model.getRotte()) {
			System.out.println(ee.toString());
			
		} 

	}

}
