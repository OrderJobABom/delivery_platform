const urlParams = new URLSearchParams(window.location.search);
const paymentKey = urlParams.get("paymentKey");
const orderId = urlParams.get("orderId");
const amount = urlParams.get("amount");

const paymentKeyElement = document.getElementById("paymentKey");
const orderIdElement = document.getElementById("orderId");
const amountElement = document.getElementById("amount");

if (paymentKeyElement) paymentKeyElement.textContent = paymentKey;
if (orderIdElement) orderIdElement.textContent = orderId;
if (amountElement) amountElement.textContent = `${amount}원`;

const confirmLoadingSection = document.querySelector('.confirm-loading');
const confirmSuccessSection = document.querySelector('.confirm-success');

async function confirmPayment() {
    try {
        const response = await fetch('/v1/payments/confirm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                paymentKey,
                orderId,
                amount: Number(amount),
            }),
        });

        if (!response.ok) {
            const errText = await response.text();
            alert("결제 승인 실패 ❌\n" + errText);
            console.error(errText);
            return;
        }

        console.log("✅ 결제 승인 성공!");
        confirmLoadingSection.style.display = 'none';
        confirmSuccessSection.style.display = 'flex';
    } catch (e) {
        console.error("❌ 결제 승인 중 오류:", e);
        alert("서버 요청 실패: " + e.message);
    }
}

const confirmPaymentButton = document.getElementById('confirmPaymentButton');
confirmPaymentButton.addEventListener('click', confirmPayment);
