import { InitiateNullificationModal } from "./initiate-nullification-modal.js";

const modal = new InitiateNullificationModal({
  async onSubmit(payload) {
    console.info("DTO for POST /api/v1/hire-nullifications", payload);
    await new Promise((resolve) => setTimeout(resolve, 450));
    window.alert(`Процесс аннулирования для ${payload.hireProcessId} подготовлен. DTO выведен в консоль.`);
  },
});

document
  .querySelector("#initiate-nullification-button")
  .addEventListener("click", () => {
    modal.open({ hireProcessId: "HIRE-2026-001245" });
  });
