CREATE TABLE gift_certificate(
    gift_id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(25) NOT NULL ,
    description VARCHAR(100) ,
    price DOUBLE NOT NULL ,
    duration INT NOT NULL ,
    create_date DATE NOT NULL ,
    last_update_date DATE NOT NULL
);

CREATE TABLE tag(
    tag_id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(25) NOT NULL
);

CREATE TABLE gift_tags(
    gift_id BIGINT NOT NULL ,
    tag_id BIGINT NOT NULL ,
    PRIMARY KEY (gift_id, tag_id),
    CONSTRAINT gift_certificate_fk FOREIGN KEY (gift_id) REFERENCES gift_certificate (gift_id) ON DELETE CASCADE ,
    CONSTRAINT tag_fk FOREIGN KEY (tag_id) REFERENCES tag (tag_id) ON DELETE CASCADE
);