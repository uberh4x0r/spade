/**
 * Copyright 2014 Dennis Rippinger
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.interactivesystems.spade.ui.action;

import info.interactivesystems.spade.dao.NilsimsaSimilarityDao;
import info.interactivesystems.spade.dao.service.ReviewContentService;
import info.interactivesystems.spade.dto.DiffContainer;
import info.interactivesystems.spade.entities.NilsimsaSimilarity;
import info.interactivesystems.spade.entities.User;
import info.interactivesystems.spade.util.DiffCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class SimilaritiesAction.
 *
 * @author Dennis Rippinger
 */
@Slf4j
@Named
@Scope("session")
public class SimilaritiesAction {

	// Resources

	@Resource
	private ReviewContentService service;

	@Resource
	private NilsimsaSimilarityDao similarityDao;

	@Resource
	private DiffCreator diffCreator;

	@Resource
	private UserAction userAction;

	@Resource
	private SimilarityBean similarityBean;

	// Variables

	@Getter
	private List<NilsimsaSimilarity> currentSimilarItem;

	@Getter
	private DiffContainer diffContainer;

	@Getter
	private NilsimsaSimilarity similarPair;

	@Getter
	@Setter
	private String currentCategory;

	@Setter
	@Getter
	private boolean userView = false;

	@Getter
	private List<SelectItem> categories;

	private Integer counter = 0;

	@Getter
	private String sessionID;

	/**
	 * Initializes with no certain category.
	 */
	@PostConstruct
	public void init() {

		sessionID = getSessionKey();

		log.info("Current Session ID: '{}'", sessionID);

		categories = new ArrayList<>();
		for (String category : similarityDao.getCategories()) {
			SelectItem categoryItem = new SelectItem();
			categoryItem.setLabel(category);
			categoryItem.setValue(category);
			categories.add(categoryItem);
		}

		next();
	}

	/**
	 * Switch to the selected author of a review and the user view.
	 *
	 * @param user
	 */
	public void switchUserView(User user) {
		userView = true;
		userAction.setUser(user);
		userAction.init();
	}

	/**
	 * Switch back to review perspective.
	 */
	public void switchUserViewBack() {
		userView = false;
	}

	/**
	 * Selects the next review in the category set.
	 */
	public void next() {
		similarPair = similarityBean.getSimilarity(counter++);
		diffContainer = diffCreator.getDifferences(similarPair.getReviewA(), similarPair.getReviewB());

		switchUserViewBack();
	}

	/**
	 * CSS for same Product
	 *
	 * @return CSS Key
	 */
	public String sameProduct() {
		String reviewIdA = similarPair.getReviewA().getProduct().getId();
		String reviewIdB = similarPair.getReviewB().getProduct().getId();

		return getCssForContentFlag(reviewIdA, reviewIdB);
	}

	/**
	 * CSS for same author
	 *
	 * @return CSS Key
	 */
	public String sameAuthor() {
		String userIdA = similarPair.getUserA().getId();
		String userIdB = similarPair.getUserB().getId();

		return getCssForContentFlag(userIdA, userIdB);
	}

	/**
	 * CSS for same rating
	 *
	 * @return CSS Key
	 */
	public String sameRating() {
		Double ratingA = similarPair.getReviewA().getRating();
		Double ratingB = similarPair.getReviewB().getRating();

		return getCssForContentFlag(ratingA, ratingB);
	}

	/**
	 * CSS for same date
	 *
	 * @return CSS Key
	 */
	public String sameDate() {
		Date dateA = similarPair.getReviewA().getReviewDate();
		Date dateB = similarPair.getReviewB().getReviewDate();

		return getCssForContentFlag(dateA, dateB);
	}

	/**
	 * CSS for same title
	 *
	 * @return CSS Key
	 */
	public String sameTitle() {
		String titleA = similarPair.getReviewA().getTitle();
		String titleB = similarPair.getReviewB().getTitle();

		return getCssForContentFlag(titleA, titleB);
	}

	/**
	 * CSS for same content
	 *
	 * @return CSS Key
	 */
	public String sameContent() {
		String titleA = similarPair.getReviewA().getContent();
		String titleB = similarPair.getReviewB().getContent();

		return getCssForContentFlag(titleA, titleB);
	}

	private String getCssForContentFlag(Object idA, Object idB) {
		if (idA.equals(idB)) {
			return "sameContent";
		}
		return "differentContent";
	}

	private String getSessionKey() {
		FacesContext fctx = FacesContext.getCurrentInstance();
		ExternalContext ectx = fctx.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);

		return session.getId();
	}
}
