package com.superluli.jpa.participant;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "participant")
public class ParticipantEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private String id;
	
	@Column(name = "promotion_id")
	private String promotionId;
	
	@Column(name = "wallet_id")
	private String walletId;

	@Column(name = "holdings")
	private int holdings;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	@Override
	public String toString() {
		return "ParticipantEntity [id=" + id + ", promotionId=" + promotionId
				+ ", walletId=" + walletId + "]";
	}

	public int getHoldings() {
		return holdings;
	}

	public void setHoldings(int holdings) {
		this.holdings = holdings;
	}
}
