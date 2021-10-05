<template>
  <div id="app">
    <v-app id="inspire">
      <div>
        <CustomerGrid></CustomerGrid>
      </div>
    </v-app>
  </div>
</template>

<script>
import CustomerGrid from "@/components/CustomerGrid";

export default {
  name: 'App',
  components: {
    CustomerGrid
  }
};
</script>
