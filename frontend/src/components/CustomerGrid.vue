<template>
  <div id="app">
    <v-app id="customer">
      <div>
        <v-banner>
          <v-row class="justify-end">
            <v-col cols="2">
              <v-select :items="countries"
                        v-model="country"
                        placeholder="Select by country"
                        hide-details
                        outlined
                        dense
                        filled
                        clearable
                        @change="onCountryChange"
              ></v-select>
            </v-col>
            <v-col cols="2">
              <v-select :items="getValidityOptions()"
                        v-model="validity"
                        placeholder="Select by validity"
                        hide-details
                        outlined
                        filled
                        dense
                        clearable
                        @change="onValidityChange"
              ></v-select>
            </v-col>
          </v-row>
        </v-banner>
        <v-data-table
            :headers="headers"
            :items="customers"
            :options.sync="options"
            :footer-props="{
                  itemsPerPageOptions: [5, 10, 15]
                }"
            :server-items-length="totalCustomers"
            :loading="loading"
            outlined
            class="elevation-1 ma-10">

          <template v-slot:top>
            <v-toolbar flat style="background:#D3D3D3 ">
              <v-toolbar-title class="tableHeader"> Customer's phonebook
              </v-toolbar-title>
            </v-toolbar>
          </template>
          <template v-slot:item.valid="{ item }">
            <v-avatar :color="getColor(item.valid)" size="20"></v-avatar>
          </template>
        </v-data-table>
      </div>
    </v-app>
  </div>
</template>
<script>
import api from "@/services/CustomerServices";

export default {
  name: 'CustomerGrid',
  data() {
    return {
      totalCustomers: 0,
      countries: [],
      validity: undefined,
      country: undefined,
      customers: [],
      loading: true,
      options: {
        "page": 1,
        "itemsPerPage": 5,
        "country": null,
        "isValid": null
      },
      headers: [
        {text: 'Name', sortable: false, value: 'name'},
        {text: 'Phone', sortable: false, value: 'phone'},
        {text: 'Country', sortable: false, value: 'countryName'},
        {text: 'Validity', sortable: false, value: 'valid'}
      ],
    }
  },
  watch: {
    options: {
      handler() {
        this.loadData()
      },
      deep: true,
    },
  },
  mounted() {
    this.getCountryOptions();
  },
  methods: {
    loadData() {
      this.loading = true;
      const args = {
        params: this.options
      }
      api.getData(args).then(reponse => {
        this.customers = reponse.data.customers;
        this.totalCustomers = reponse.data.numberOfItems;
        this.loading = false;
      });
    },
    getCountryOptions() {
      this.loading = true;
      api.getCountryOptions().then(reponse => {
        this.countries = reponse.data;
        this.loading = false;
      });
    },
    getValidityOptions() {
      return [
        {text: "Valid", value: true},
        {text: "Invalid", value: false}
      ];
    },
    onValidityChange(value) {
      this.options.page = 1;
      this.options.isValid = value;
    },
    onCountryChange(value) {
      this.options.page = 1;
      this.options.country = value;
    },
    getColor(value) {
      if (value) {
        return "green";
      }
      return "red";
    }
  }
};
</script>

<style>
.tableHeader {
  color: blue;
  font-size: xx-large;
  font-weight: bold;
}
</style>
