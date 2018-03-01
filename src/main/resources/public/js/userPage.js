$(document).ready(function(){
   $(".delete_food").each(function() {
       this.click(deleteFood(event));
   });
   function deleteFood(event) {
       let data = {
           "orderId": this.data("orderId"),
           "foodId": this.data("foodId")
       }
       $.ajax({
           type: "POST",
           url: "/api/food/delete/",
           data: JSON.stringify(data),
           dataType: "json",
           contentType: "application/json",
           success: function (response) {
               buildFoodContainer(response);
           }
       });

       $.ajax({
           type: "POST",
           url: "/api/getfullorderPrice/",
           data: JSON.stringify({
               "orderId": this.data("orderId")
           }),
           dataType: "json",
           contentType: "application/json",
           success: function (response) {
               let fullPriceContainer = $(".foodFullPrice");
               fullPriceContainer.empty();
               fullPriceContainer.text(response.fullPrice);
           }
       });
   }

   function buildFoodContainer(response) {
       let targetDiv = $(".foodContainer");
       targetDiv.empty();
       for (let i = 1; i < response.length; i++) {
           targetDiv.append(buildFoodContainerElement(
               response[i].foodId,
               response[i].orderId,
               response[i].foodPrice,
               response[i].foodName
           ));
       }
   }

   function buildFoodContainerElement(foodId, orderId, foodPrice, foodName) {
       let wrapper = $(".foodContainer");
       let foodInfoSpan = $('<span>').text(foodName + '  ' +  foodPrice);
       let deleteLink = $('<a>', {"class":"delete_food"})
           .attr("data-orderId", orderId)
           .attr("data-foodId", foodId)
           .attr("href", "")
           .text("Cancel");

       wrapper.append(foodInfoSpan).append(deleteLink);

       return wrapper;
   }

});