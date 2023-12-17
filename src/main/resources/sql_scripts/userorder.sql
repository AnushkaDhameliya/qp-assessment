CREATE TABLE IF NOT EXISTS public.user_order
(
    id bigint NOT NULL DEFAULT nextval('inventory_id_seq'::regclass),
    purchase_id bigint NOT NULL,
    user_id bigint,
    item_id bigint,
    quantity integer,
    weight numeric(7,2),
    ordered_date date NOT NULL,
    active boolean,
    CONSTRAINT order_id_pkey PRIMARY KEY (id),
    CONSTRAINT item_order_id FOREIGN KEY (item_id)
        REFERENCES public.item (id),
    CONSTRAINT user_order_id FOREIGN KEY (user_id)
        REFERENCES public.user_detail (userid)
);

CREATE SEQUENCE IF NOT EXISTS public.order_id_seq;


CREATE SEQUENCE IF NOT EXISTS public.order_purchase_id_seq;
