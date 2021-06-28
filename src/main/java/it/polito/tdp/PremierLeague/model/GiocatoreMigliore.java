package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	Player p;
	Double punteggio;
	
	public GiocatoreMigliore(Player p, Double punteggio) {
		super();
		this.p = p;
		this.punteggio = punteggio;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public Double getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(Double punteggio) {
		this.punteggio = punteggio;
	}
	@Override
	public String toString() {
		return "GiocatoreMigliore [p=" + p + ", punteggio=" + punteggio + "]";
	}
	
	

}
