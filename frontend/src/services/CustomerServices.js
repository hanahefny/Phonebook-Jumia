import axios from "axios";

const baseUrl = "http://localhost:8080/";

const api = {
  async getCountryOptions() {
    return axios.get(baseUrl + "/customers/countries");
  },

  async getData(params) {
    return axios.get(baseUrl + "/customers", params);
  },
};

export default api;