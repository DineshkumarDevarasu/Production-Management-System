package com.witsoman.maharaja;

import java.util.Date;


public class  ChefProduction {
        public long UniqueId ;
        public int OrderId ;
        public int ChefId;
        public int ProductId ;
        public String ProductName ;
        public Date StartTime;
        public Date EndTime;
        public double Qty ;
        public int VerifiedChef ;
        public int CreatedUser ;
        public int UpdatedUser;

        public String getProductName() {
                return ProductName;
        }

        public void setProductName(String productName) {
                ProductName = productName;
        }

        public long getUniqueId() {
                return UniqueId;
        }

        public void setUniqueId(long uniqueId) {
                UniqueId = uniqueId;
        }

        public int getOrderId() {
                return OrderId;
        }

        public void setOrderId(int orderId) {
                OrderId = orderId;
        }

        public int getChefId() {
                return ChefId;
        }

        public void setChefId(int chefId) {
                ChefId = chefId;
        }

        public int getProductId() {
                return ProductId;
        }

        public void setProductId(int productId) {
                ProductId = productId;
        }

        public Date getStartTime() {
                return StartTime;
        }

        public void setStartTime(Date startTime) {
                StartTime = startTime;
        }

        public Date getEndTime() {
                return EndTime;
        }

        public void setEndTime(Date endTime) {
                EndTime = endTime;
        }

        public double getQty() {
                return Qty;
        }

        public void setQty(double qty) {
                Qty = qty;
        }

        public int getVerifiedChef() {
                return VerifiedChef;
        }

        public void setVerifiedChef(int verifiedChef) {
                VerifiedChef = verifiedChef;
        }

        public int getCreatedUser() {
                return CreatedUser;
        }

        public void setCreatedUser(int createdUser) {
                CreatedUser = createdUser;
        }

        public int getUpdatedUser() {
                return UpdatedUser;
        }

        public void setUpdatedUser(int updatedUser) {
                UpdatedUser = updatedUser;
        }



}
