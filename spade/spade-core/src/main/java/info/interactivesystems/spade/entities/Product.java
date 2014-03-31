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
package info.interactivesystems.spade.entities;

import info.interactivesystems.spade.util.ProductCategory;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for a Product.
 * 
 * @author Dennis Rippinger
 */
@Entity
@Getter
@Setter
@Table(name = "Products")
public class Product {

    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    private ProductCategory type;

    private double rating;

    private String imageUrl;

    private String source;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Review> reviews;

}
