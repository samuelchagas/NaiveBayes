package br.ufu.naivebayes.modelos;

import java.util.Arrays;

public class Amostra {

	private String classe;
	private double atributos[];

	public Amostra() {
		super();
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public double[] getAtributos() {
		return atributos;
	}

	public void setAtributos(double[] atributos) {
		this.atributos = atributos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(atributos);
		result = prime * result + ((classe == null) ? 0 : classe.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amostra other = (Amostra) obj;
		if (!Arrays.equals(atributos, other.atributos))
			return false;
		if (classe == null) {
			if (other.classe != null)
				return false;
		} else if (!classe.equals(other.classe))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Amostra [classe=" + classe + ", atributos=" + Arrays.toString(atributos) + "]";
	}

}
