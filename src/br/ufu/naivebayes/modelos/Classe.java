package br.ufu.naivebayes.modelos;

import java.util.Arrays;

public class Classe {

	private String id;
	private double mediaAtributo[];
	private double dpAtributo[];

	public Classe() {
		super();
	}

	public Classe(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double[] getMediaAtributo() {
		return mediaAtributo;
	}

	public void setMediaAtributo(double[] mediaAtributo) {
		this.mediaAtributo = mediaAtributo;
	}

	public double[] getDpAtributo() {
		return dpAtributo;
	}

	public void setDpAtributo(double[] dpAtributo) {
		this.dpAtributo = dpAtributo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Classe other = (Classe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Classe [id=" + id + ", mediaAtributo=" + Arrays.toString(mediaAtributo) + ", dpAtributo=" + Arrays.toString(dpAtributo) + "]";
	}

}
